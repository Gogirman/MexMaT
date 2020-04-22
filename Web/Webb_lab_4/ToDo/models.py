from django.db import models

# Create your models here.


class Todo(models.Model):
    login = models.CharField(max_length=50)
    name = models.CharField(max_length=50)
    description = models.TextField()
    is_complete = models.BooleanField()
