"""@package pad.models
@author: Zosia Sobocinska
@date Mar 16, 2014
"""

from backbone.models import Note
import Queue as queue
from multiprocessing import Lock
from diffmatchpatch.diff_match_patch import diff_match_patch


class Pad(object):

    general_lock = Lock()
    pads = {}

    def __init__(self, note_id, on_sync_start, on_sync_end):
        self.note = Note.objects.get(pk=note_id)
        self.note_id = note_id
        self.lock = Lock()
        self.patch_queue = queue.Queue()
        self.on_sync_start = on_sync_start
        self.on_sync_end = on_sync_end
        self.dmp = diff_match_patch()

    @staticmethod
    def validate(patch):
        return patch.replace("&nbsp;", " ")  # @todo validation && sanitization

    def process(self, patch):
        patch = self.validate(patch)
        self.lock.acquire()
        try:
            self.patch_queue.put(patch)
        finally:
            self.lock.release()
        return patch

    @staticmethod
    def get_pad(note_id):
        Pad.general_lock.acquire()
        try:
            if note_id in Pad.pads:
#                 Pad.pads[note_id].on_sync_start()
                Pad.pads[note_id].apply_diffs()
#                 Pad.pads[note_id].on_sync_end()
            else:
                Pad.pads[note_id] = Pad(note_id, None, None)
        finally:
            Pad.general_lock.release()
        if note_id in Pad.pads:
            return Pad.pads[note_id]
        else:
            raise Exception("Can't get pad")

    def apply_diffs(self):
        self.lock.acquire()
        try:
            #self.on_sync_start()
            note = self.note
            try:
                while not self.patch_queue.empty():
                    patch_text = self.patch_queue.get(True)
                    print "-----------------PATCH!!!-----------------"
                    patches = self.dmp.patch_fromText(patch_text)
                    note.content = self.dmp.patch_apply(patches, note.content)[0]
            except queue.Empty:
                pass
            note.save()
            #self.on_sync_end()
        finally:
            self.lock.release()
