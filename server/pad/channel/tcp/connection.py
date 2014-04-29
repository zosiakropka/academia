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
        self.pad_id = None
        self.pad = None
        self.conn_id = None

        sock = kwargs['sock'] if 'sock' in kwargs else args[0]
        dispatcher_with_send.__init__(self, *args, **kwargs)
        sock.setblocking(0)

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
            if purpose == "join" and "message" in data:
                self.pad_id = data.pop("message")
                self.pad = Pad.get_pad(self.pad_id)
            elif purpose == "patches" and "message" in data and self.pad_id:
                try:
                    token = data.pop("token", None)
                    tokens.validate(
                        token,
                        scope.access_obj(Note.objects.get(pk=self.pad_id), "edit"),
                    )
                    data["message"] = self.pad.process(data["message"])

                    print data["message"]

                    self.pad_broadcast(data)
                except Exception:
                    response = {
                        "purpose": "auth",
                        "message": "fail"
                    }
                    self.send_record(response)

    def pad_broadcast(self, data):
        """
        Broadcast message within current pad.
        """
        self.pad_server.pad_broadcast(encode(data), self.pad_id, self.conn_id)

    def send_record(self, record):
        dispatcher_with_send.send(self, "%s%s%s" % (record, DELIMITER, '\n'))
