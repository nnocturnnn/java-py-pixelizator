from typing import Union

from django.http import HttpRequest, HttpResponse, HttpResponseRedirect
from django.shortcuts import get_object_or_404, render
from PIL import Image as Pillow

from .forms import AddForm, ChangeForm
from .misc import pixelate_image, resize_image, save_image_from_url
from .models import ChangedImage, Image


def index(request: HttpRequest) -> HttpResponse:
    images = Image.objects.all()
    context = {"images": images}
    return render(request, "image/index.html", context)


def one_image(request: HttpRequest, image_id: int) -> HttpResponse:
    image = get_object_or_404(Image, id=image_id)
    proportion = image.width / image.height
    if request.method == "POST":
        form = ChangeForm(request.POST)
        if form.is_valid():
            width = form.cleaned_data["width"]
            height = form.cleaned_data["height"]
            try:
                width = int(width) if width else int(height * proportion)
                height = int(height) if height else int(width / proportion)
            except ValueError:
                width, height = image.width, image.height

            pil_image = Pillow.open(image.file.path)
            pixel_size = width - height
            pil_image = pixelate_image(pil_image, pixel_size)
            new_pic = resize_image(pil_image, width, height)

            changed_image = ChangedImage.objects.create(file=new_pic)
            context = {"image": changed_image, "form": form}
            return render(request, "image/image.html", context)
    else:
        form = ChangeForm()

    context = {"image": image, "form": form}
    return render(request, "image/image.html", context)


def new_image(request: HttpRequest) -> Union[HttpResponse, HttpResponseRedirect]:
    if request.method == "POST":
        form = AddForm(request.POST, request.FILES)
        if form.is_valid():
            img_url = form.cleaned_data.get("url")
            img_file = form.cleaned_data.get("file")
            if img_file:
                image = form.save()
            elif img_url:
                new_pic = save_image_from_url(img_url)
                image = Image.objects.create(file=new_pic)
            return HttpResponseRedirect(f"../id/{image.id}")
    else:
        form = AddForm()

    context = {"form": form}
    return render(request, "image/new_image.html", context)
