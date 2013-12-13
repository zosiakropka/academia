#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""@package backbon.tests
@author: Zosia Sobocinska
@date Dec 13, 2013
"""

from django.test import TestCase


class SimpleTest(TestCase):
    def test_basic_addition(self):
        """
        Tests that 1 + 1 always equals 2.
        """
        self.assertEqual(1 + 1, 2)
