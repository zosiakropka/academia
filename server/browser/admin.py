"""@package browser.admin
@author: Zosia Sobocinska
@date Nov 26, 2013
"""

from django.contrib import admin
from pad.models import Pad
from browser.models import Subject, Activity, Note

admin.site.register(Activity)
admin.site.register(Subject)


class NoteAdmin(admin.ModelAdmin):
    pass
    #fields = ['access', 'date', 'owner', 'activity']

admin.site.register(Note, NoteAdmin)

admin.site.register(Pad)
