#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package pad
@author: Zosia Sobocinska
@date Nov 2, 2013
"""
from backbone.models import Note


class Pad(object):

    def __init__(self, note_id,):
        self.note = Note.objects.get(pk=note_id)
        self.note_id = note_id

    @staticmethod
    def validate(patch):
        return patch.replace("&nbsp;", " ")

    def process(self, patch):
        patch = self.validate(patch)
        # @todo process patch, eg. store patch to apply it some time
        return patch
