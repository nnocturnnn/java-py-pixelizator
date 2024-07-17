import os

from django.db.models import ImageField, Model, PositiveIntegerField, URLField


class Image(Model):
    file = ImageField(
        upload_to="origin/",
        blank=True,
        height_field="height",
        width_field="width",
    )
    width = PositiveIntegerField(default=file.width_field, null=True, blank=True)
    height = PositiveIntegerField(default=file.height_field, null=True, blank=True)
    url = URLField(null=True, blank=True)

    def __str__(self):
        filename = os.path.basename(self.file.name)
        return filename


class ChangedImage(Model):
    file = ImageField(upload_to="changed")

    def __str__(self):
        filename = os.path.basename(self.file.name)
        return filename
