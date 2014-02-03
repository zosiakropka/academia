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
    CHUNK_SIZE = 1024
    CHNL = ""
    buffer = ""
    pad_server = None
    pad = None
    conn_id = None

    def __init__(self):
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
                self.pad = data.pop("message")
            elif purpose == "patches":
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
                    self.send(encode(response))
            elif purpose == "test" and "message" in data:
                logging.debug("Test message received: %s", data["message"])

    def close(self):
        self.close()

    def pad_broadcast(self, data):
        self.pad_server.pad_broadcast(encode(data), self.pad, self.conn_id)
