# -*- coding: utf-8 -*-
# Generated by Django 1.11.2 on 2017-07-04 18:06
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('deals', '0003_mobileverifyrecord_userprofile'),
    ]

    operations = [
        migrations.AlterField(
            model_name='userprofile',
            name='mobile',
            field=models.CharField(default='', max_length=11, verbose_name='手机号'),
        ),
    ]
