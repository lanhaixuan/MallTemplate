# -*- coding: utf-8 -*-
# Generated by Django 1.11.2 on 2017-07-05 10:12
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('deals', '0010_order_orderitems'),
    ]

    operations = [
        migrations.AlterField(
            model_name='order',
            name='amount',
            field=models.FloatField(blank=True, null=True, verbose_name='订单总价'),
        ),
    ]
