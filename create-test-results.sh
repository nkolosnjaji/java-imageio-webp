#! /bin/bash

rm src/test/resources/images/cwebp/*.webp

# images for writer

cwebp -quiet src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb.webp
cwebp -quiet src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba.webp

cwebp -quiet -m 0 src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_m0.webp
cwebp -quiet -m 0 src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_m0.webp

cwebp -quiet -q 50 src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_q50.webp
cwebp -quiet -q 50 src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_q50.webp

cwebp -quiet -preset icon src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_presetIcon.webp
cwebp -quiet -preset icon src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_presetIcon.webp

cwebp -quiet -hint photo src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_hintPhoto.webp
cwebp -quiet -hint photo src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_hintPhoto.webp

cwebp -quiet -resize 200 100 src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_resize200100.webp
cwebp -quiet -resize 200 100 src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_resize200100.webp

cwebp -quiet -crop 100 100 100 100 src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_crop100100100100.webp
cwebp -quiet -crop 100 100 100 100 src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_crop100100100100.webp

cwebp -quiet -crop 100 100 100 100 -resize 200 100 src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_resize100100_crop100100200100.webp
cwebp -quiet -crop 100 100 100 100 -resize 200 100 src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_resize100100_crop100100200100.webp

cwebp -quiet -lossless src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_lossless.webp
cwebp -quiet -lossless src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_lossless.webp

cwebp -quiet -sharpness 6 icon src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_sharpness.webp
cwebp -quiet -sharpness 6 icon src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_sharpness.webp

cwebp -quiet -alpha_filter best src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_alphaFilter.webp
cwebp -quiet -alpha_filter best src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_alphaFilter.webp

cwebp -quiet -af src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_autoFilter.webp
cwebp -quiet -af src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_autoFilter.webp

cwebp -quiet -sharp_yuv src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_sharpYuv.webp
cwebp -quiet -sharp_yuv src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_sharpYuv.webp

# images for reader

cwebp -quiet -q 100 -m 6 -lossless src/test/resources/images/input/reader_rgb.png -o src/test/resources/images/cwebp/reader_rgb.webp
cwebp -quiet -q 100 -m 6 -lossless src/test/resources/images/input/reader_rgba.png -o src/test/resources/images/cwebp/reader_rgba.webp
