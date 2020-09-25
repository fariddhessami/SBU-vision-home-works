Image = imread('US-grant.jpg');

ImageGray = rgb2gray(Image);

imshow(ImageGray);

histogram(Image);