# Generated by Django 2.2.7 on 2019-11-05 07:21

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('ToDo', '0001_initial'),
    ]

    operations = [
        migrations.RenameField(
            model_name='todo',
            old_name='text',
            new_name='description',
        ),
        migrations.AddField(
            model_name='todo',
            name='name',
            field=models.CharField(default=1, max_length=50),
            preserve_default=False,
        ),
    ]
