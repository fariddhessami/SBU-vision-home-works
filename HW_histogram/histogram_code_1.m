Image = imread('US-grant.jpg');

ImageGray = rgb2gray(Image);

imshow(ImageGray);

FreqPerIntense = zeros(256,1);

ImageSize = size(ImageGray);
HeightSize= ImageSize(1);
WidthSize = ImageSize(2);

for I_index = 1:WidthSize
    for J_index = 1:HeightSize
        Intensity = ImageGray(J_index,I_index);
        FreqPerIntense(Intensity)=FreqPerIntense(Intensity)+1;
    end
end

bar(FreqPerIntense);