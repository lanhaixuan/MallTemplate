# -*- coding: utf-8 -*-
# Generated by Django 1.11.2 on 2017-07-05 09:18
from __future__ import unicode_literals

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('deals', '0008_auto_20170705_0110'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='customers',
            name='owner',
        ),
    ]
