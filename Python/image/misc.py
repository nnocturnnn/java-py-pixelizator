import sys
from io import BytesIO

import requests
from django.core.files.base import ContentFile
from django.core.files.uploadedfile import InMemoryUploadedFile
from PIL import Image
from PIL import Image as Pillow


def resize_image(pil_image: Image, width: int, height: int) -> InMemoryUploadedFile:
    resized_image = pil_image.resize((width, height), Pillow.ANTIALIAS)
    buffer = BytesIO()
    resized_image.save(buffer, format="JPEG")
    buffer.seek(0)
    return InMemoryUploadedFile(
        buffer,
        "ImageField",
        "resized_image.jpg",
        "image/jpeg",
        sys.getsizeof(buffer),
        None,
    )


def pixelate_image(pil_image: Image, pixel_size: int) -> Image:
    pix = pil_image.load()
    width, height = pil_image.size

    for startX in range(0, width, pixel_size):
        for startY in range(0, height, pixel_size):
            deltaX = min(pixel_size, width - startX)
            deltaY = min(pixel_size, height - startY)
            quadrant_pixels = [
                pix[x, y]
                for x in range(startX, startX + deltaX)
                for y in range(startY, startY + deltaY)
            ]
            avg_pixel = tuple(
                sum(values) // len(quadrant_pixels) for values in zip(*quadrant_pixels)
            )
            for x in range(startX, startX + deltaX):
                for y in range(startY, startY + deltaY):
                    pix[x, y] = avg_pixel

    return pil_image


def save_image_from_url(img_url: str) -> InMemoryUploadedFile:
    name = img_url.split("/")[-1]
    response = requests.get(img_url)
    image_content = ContentFile(response.content)
    return InMemoryUploadedFile(
        image_content,
        "ImageField",
        name,
        "image/jpeg",
        sys.getsizeof(image_content),
        None,
    )
