#!/usr/bin/env python3
"""Generate launcher icon webp assets for all mipmap densities."""

from __future__ import annotations

import math
from pathlib import Path

from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[1]
RES = ROOT / "app" / "src" / "main" / "res"

DENSITIES: dict[str, int] = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192,
}

GRADIENT_START = (0x63, 0x66, 0xF1)
GRADIENT_END = (0x8B, 0x5C, 0xF6)
TRICOLOR_BLUE = (0x00, 0x55, 0xA4)
TRICOLOR_WHITE = (0xFF, 0xFF, 0xFF)
TRICOLOR_RED = (0xEF, 0x41, 0x35)
INDIGO = (0x63, 0x66, 0xF1)
PAGE_LEFT = (0xFF, 0xFF, 0xFF)
PAGE_RIGHT = (0xF1, 0xF5, 0xF9)


def lerp(a: int, b: int, t: float) -> int:
    return int(a + (b - a) * t)


def gradient_background(size: int) -> Image.Image:
    img = Image.new("RGBA", (size, size))
    px = img.load()
    for y in range(size):
        for x in range(size):
            t = (x / max(size - 1, 1) + y / max(size - 1, 1)) / 2.0
            r = lerp(GRADIENT_START[0], GRADIENT_END[0], t)
            g = lerp(GRADIENT_START[1], GRADIENT_END[1], t)
            b = lerp(GRADIENT_START[2], GRADIENT_END[2], t)
            px[x, y] = (r, g, b, 255)
    return img


def scale_point(x: float, y: float, size: int) -> tuple[float, float]:
    return (x / 108.0 * size, y / 108.0 * size)


def draw_book(draw: ImageDraw.ImageDraw, size: int) -> None:
    def pt(x: float, y: float) -> tuple[float, float]:
        return scale_point(x, y, size)

    left_page = [pt(22, 34), pt(52, 30), pt(52, 74), pt(22, 78)]
    right_page = [pt(56, 30), pt(86, 34), pt(86, 78), pt(56, 74)]
    draw.polygon(left_page, fill=PAGE_LEFT)
    draw.polygon(right_page, fill=PAGE_RIGHT)

    spine_w = 2.5 / 108.0 * size
    cx = 53.75 / 108.0 * size
    top = 30 / 108.0 * size
    bottom = 74 / 108.0 * size
    third = spine_w / 3.0
    x0 = cx - spine_w / 2.0
    draw.rectangle([x0, top, x0 + third, bottom], fill=TRICOLOR_BLUE)
    draw.rectangle([x0 + third, top, x0 + 2 * third, bottom], fill=TRICOLOR_WHITE)
    draw.rectangle([x0 + 2 * third, top, x0 + spine_w, bottom], fill=TRICOLOR_RED)

    line_h = max(1, int(round(2.5 / 108.0 * size)))
    def hline(x1: float, y: float, x2: float) -> None:
        p1 = pt(x1, y)
        p2 = pt(x2, y + 2.5)
        draw.rectangle([p1[0], p1[1], p2[0], p2[1]], fill=INDIGO)

    hline(28, 42, 46)
    hline(28, 48, 42)
    hline(28, 54, 44)
    hline(62, 42, 80)
    hline(62, 48, 74)

    qx, qy = pt(71, 51)
    qr = 5.0 / 108.0 * size
    draw.arc(
        [qx - qr, qy - qr - qr * 0.3, qx + qr, qy + qr * 0.5],
        start=200,
        end=-20,
        fill=INDIGO,
        width=max(2, line_h),
    )
    dot_r = max(1, int(round(2.0 / 108.0 * size)))
    dot_c = pt(73.5, 68)
    draw.ellipse(
        [
            dot_c[0] - dot_r,
            dot_c[1] - dot_r,
            dot_c[0] + dot_r,
            dot_c[1] + dot_r,
        ],
        fill=INDIGO,
    )


def composite_icon(size: int, round_mask: bool) -> Image.Image:
    base = gradient_background(size)
    draw = ImageDraw.Draw(base)
    draw_book(draw, size)
    if round_mask:
        mask = Image.new("L", (size, size), 0)
        md = ImageDraw.Draw(mask)
        md.ellipse([0, 0, size - 1, size - 1], fill=255)
        base.putalpha(mask)
    return base


def foreground_icon(size: int) -> Image.Image:
    img = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    draw_book(draw, size)
    return img


def save_webp(path: Path, img: Image.Image) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    img.save(path, format="WEBP", quality=95, method=6)


def main() -> None:
    for folder, size in DENSITIES.items():
        out_dir = RES / folder
        save_webp(out_dir / "ic_launcher.webp", composite_icon(size, round_mask=False))
        save_webp(out_dir / "ic_launcher_round.webp", composite_icon(size, round_mask=True))
        save_webp(out_dir / "ic_launcher_foreground.webp", foreground_icon(size))
        print(f"Wrote {folder} ({size}px)")


if __name__ == "__main__":
    main()
