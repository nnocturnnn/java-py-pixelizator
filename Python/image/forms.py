import requests
from django.forms import ModelForm, ValidationError

from .models import Image


class AddImageForm(ModelForm):
    class Meta:
        model = Image
        fields = ["url", "file"]

    def clean(self):
        cleaned_data = super().clean()
        file = cleaned_data.get("file")
        url = cleaned_data.get("url")

        if not file and not url:
            raise ValidationError("One of the fields is required.")

        if file and url:
            raise ValidationError("Only one of the fields is allowed.")

        if url:
            try:
                response = requests.head(url)
                content_type = response.headers.get("Content-Type")
                if not content_type or not content_type.startswith("image/"):
                    raise ValidationError("Invalid URL format.")
            except requests.RequestException:
                raise ValidationError("Invalid URL.")

        return cleaned_data


class ChangeImageForm(ModelForm):
    class Meta:
        model = Image
        fields = ["width", "height"]

    def clean(self):
        cleaned_data = super().clean()
        width = cleaned_data.get("width")
        height = cleaned_data.get("height")

        if not width and not height:
            raise ValidationError("At least one of the fields is required.")

        return cleaned_data
