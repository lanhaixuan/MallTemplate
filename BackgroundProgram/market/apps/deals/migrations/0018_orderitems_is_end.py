# -*- coding: utf-8 -*-
# Generated by Django 1.11.2 on 2017-07-06 00:19
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('deals', '0017_auto_20170705_2229'),
    ]

    operations = [
        migrations.AddField(
            model_name='orderitems',
            name='is_end',
            field=models.BooleanField(default=False, verbose_name='订单上传结束'),
        ),
    ]
