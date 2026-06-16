#!/usr/bin/env python3
"""Generate launcher icon webp assets from design/ic_launcher.png."""

from __future__ import annotations

from pathlib import Path

from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "design" / "ic_launcher.png"
RES = ROOT / "app" / "src" / "main" / "res"

DENSITIES: dict[str, int] = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192,
}


def fit_square(image: Image.Image, size: int) -> Image.Image:
    return image.resize((size, size), Image.Resampling.LANCZOS)


def round_mask(image: Image.Image) -> Image.Image:
    size = image.size[0]
    mask = Image.new("L", (size, size), 0)
    draw = ImageDraw.Draw(mask)
    draw.ellipse([0, 0, size - 1, size - 1], fill=255)
    rounded = image.copy()
    rounded.putalpha(mask)
    return rounded


def save_webp(path: Path, image: Image.Image) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    image.save(path, format="WEBP", quality=95, method=6)


def main() -> None:
    if not SOURCE.is_file():
        raise SystemExit(f"Missing source icon: {SOURCE}")

    source = Image.open(SOURCE).convert("RGBA")

    for folder, size in DENSITIES.items():
        out_dir = RES / folder
        square = fit_square(source, size)
        save_webp(out_dir / "ic_launcher.webp", square)
        save_webp(out_dir / "ic_launcher_round.webp", round_mask(square))
        save_webp(out_dir / "ic_launcher_foreground.webp", square)
        print(f"Wrote {folder} ({size}px)")


if __name__ == "__main__":
    main()
