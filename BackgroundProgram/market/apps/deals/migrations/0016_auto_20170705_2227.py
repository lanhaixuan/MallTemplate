# -*- coding: utf-8 -*-
# Generated by Django 1.11.2 on 2017-07-05 22:27
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('deals', '0015_auto_20170705_2218'),
    ]

    operations = [
        migrations.AddField(
            model_name='order',
            name='ordernum',
            field=models.IntegerField(default=0, verbose_name='订单号'),
        ),
        migrations.AlterField(
            model_name='order',
            name='amount',
            field=models.FloatField(default=0, verbose_name='订单总价'),
        ),
    ]
