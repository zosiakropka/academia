#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad.channel.base.connection
@author: Zosia Sobocinska
@date Dec 11, 2013
"""
import logging
from pad.channel.endec import encode, decode
from access_tokens import tokens, scope
from backbone.models import Note


class PadBaseConnection():

    def __init__(self):
        self.CHUNK_SIZE = 1024
        self.CHNL = ""
        self.buffer = ""
        self.pad_server = None
        self.pad_id = None
        self.conn_id = None

        logging.info("%s client" % (self.CHNL))

    def set_server(self, server):
        self.pad_server = server

    def handle_close(self):
        logging.debug("%s : %s disconnected" % (self.CHNL, str(self.socket.getpeername())))

    def handle_message(self, message):
        data = decode(message)
        if data and "purpose" in data:
            logging.debug("%s has data onboard." % (self.CHNL))
            purpose = data["purpose"]
            if purpose == "pad":
                self.pad_id = data.pop("message")
            elif purpose == "patches" and self.pad_id:
                try:
                    token = data.pop("token", None)
                    tokens.validate(
                        token,
                        scope.access_obj(Note.objects.get(pk=self.pad_id), "edit"),
                    )
                    self.pad_broadcast(data)
                except Exception:
                    response = {
                        "purpose": "auth",
                        "message": "fail"
                    }
                    self.send(encode(response))

    def close(self):
        self.close()

    def pad_broadcast(self, data):
        self.pad_server.pad_broadcast(encode(data), self.pad_id, self.conn_id)
