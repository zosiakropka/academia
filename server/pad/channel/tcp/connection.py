# -*- coding: UTF-8 -*-
"""@package pad.channel.tcp.connection
@author: Zosia Sobocinska
@date Dec 11, 2013
"""
from asyncore import dispatcher_with_send
import logging
from pad.channel.endec import encode, decode
from access_tokens import tokens, scope
from backbone.models import Note
from pad.channel.tcp import DELIMITER, CHUNK_SIZE
from pad.models import Pad


class PadTCPConnection(dispatcher_with_send):

    CHNL = "TCPConnection"

    def __init__(self, *args, **kwargs):
        logging.info("%s client" % (self.CHNL))

        self.buffer = ""
        self.pad_server = kwargs.pop('pad_server')
        self.pad = None
        self.conn_id = None
        self.user = None

        sock = kwargs['sock'] if 'sock' in kwargs else args[0]
        dispatcher_with_send.__init__(self, *args, **kwargs)
        sock.setblocking(0)

    @property
    def ready(self):
        return self.user and self.pad

    def handle_read(self):
        """
        Low-level receiving messages. Calls handle_message() on each complete
        message record.
        """
        data = None
        try:
            data = self.recv(CHUNK_SIZE)
        except:
            pass
        if data:
            self.buffer += data
            if DELIMITER in self.buffer:
                records = self.buffer.split(DELIMITER)
                self.buffer = records.pop()
                for record in records:
                    self.handle_message(record)

    def handle_message(self, record):
        """
        Try to decode messge record and process it adequately. Should be
        called by handle_read() when receiving single record is complete.
        """
        data = decode(record)
        if data and "purpose" in data:
            logging.debug("%s has data onboard, purpose: %s" % (self.CHNL, data["purpose"]))
            purpose = data["purpose"]
            if purpose == "join" and "login" in data and "message" in data:
                self.pad = Pad.get_pad(data.pop("message"))
                self.user = User.objects.get(username=data.pop("login"))
            elif purpose == "patches" and "message" in data and self.ready:
                try:
                    # @todo tokens don't seem to work...
                    token = data.pop("token", None)
                    tokens.validate(
                        token,
                        scope.access_obj(self.pad.note, "edit"),
                    )
                    data["message"] = self.pad.append_patch(self.user, data["message"])

                    self.pad_broadcast(data)
                except Exception:
                    response = {
                        "purpose": "auth",
                        "message": "fail"
                    }
                    self.send_record(response)

    def handle_close(self):
        self.pad.apply_patches()
        self.close()

    def pad_broadcast(self, data):
        """
        Broadcast message within current pad.
        """
        self.pad_server.pad_broadcast(encode(data), self.pad.note.pk, self.conn_id)

    def send_record(self, record):
        dispatcher_with_send.send(self, "%s%s%s" % (record, DELIMITER, '\n'))
