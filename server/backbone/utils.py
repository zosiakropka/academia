"""@package backbone.utils
@author: Zosia Sobocinska
@date Apr 1, 2014
"""


class classproperty(property):
    def __get__(self, cls, owner):
        return classmethod(self.fget).__get__(None, owner)()
