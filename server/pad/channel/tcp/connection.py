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


class PadTCPConnection(dispatcher_with_send):

    CHNL = "TCPConnection"

    def __init__(self, *args, **kwargs):
        logging.info("%s client" % (self.CHNL))
        sock = kwargs['sock'] if 'sock' in kwargs else args[0]
        dispatcher_with_send.__init__(self, *args, **kwargs)
        sock.setblocking(0)

        self.buffer = ""
        self.pad_server = None
        self.pad = None
        self.conn_id = None

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
        if (data):
            self.buffer += data
            if DELIMITER in self.buffer:
                records = self.buffer.split(DELIMITER)
                self.buffer = records.pop()
                for record in records:
                    print "RECORD: %s" % record
                    self.handle_message(record)

    def handle_message(self, record):
        """
        Try to decode messge record and process it adequately. Should be
        called by handle_read() when receiving single record is complete.
        """
        print "NEW MESSAGE"
        data = decode(record)
        if data and "purpose" in data:
            logging.debug("%s has data onboard." % (self.CHNL))
            purpose = data["purpose"]
            if purpose == "pad":
                self.pad = data.pop("message")
            elif purpose == "patches" and self.pad:
                print "PURPOSE: PATCHES"
                try:
                    token = data.pop("token", None)
                    tokens.validate(
                        token,
                        scope.access_obj(Note.objects.get(pk=self.pad), "edit"),
                    )
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
        print "PAD BROADCAST FROM TCP CONNECTION"
        self.pad_server.pad_broadcast(encode(data), self.pad, self.conn_id)

    def send_record(self, record):
        print "SENDING RECORD"
        dispatcher_with_send.send(self, "%s%s%s" % (record, DELIMITER, '\n'))
