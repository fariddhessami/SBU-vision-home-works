ImageToModify = imread('picture-Low.png');
ImageToModifyGray = rgb2gray(ImageToModify);
Hist1 = histogram(ImageToModifyGray);

ResultImage = ImageToModifyGray;

X = Hist1.Data;
Y = X;
HistValues = Hist1.Values;

[LengthSize,WidthSize] = size(ImageToModifyGray);


MinValue = min(min(Y));
MaxValue = max(max(Y));


Wish_MinValue = 0;
Wish_MaxValue = 255;

isfloat(sampleDivide);
fmax = float(MaxValue);
fmin = float(MaxValue);

sampleDivide = fmax / fmin;

% for I_index = 1:WidthSize
%     for J_index = 1:LengthSize
%         if X[] >=
            
%         end
%     end
% end


ModifiedImage = imread('picture-example-Low.png');



figure;
subplot(3, 2, 1); imshow(ImageToModify); title('ImageToModify');
subplot(3, 2, 2); histogram(ImageToModify); title('Hist1');
subplot(3, 2, 3); imshow(ModifiedImage); title('ModifiedImage');
subplot(3, 2, 4); histogram(ModifiedImage); title('Hist2');
subplot(3, 2, 5); imshow(ModifiedImage); title('ModifiedImage');
subplot(3, 2, 6); histogram(ModifiedImage); title('Hist2');

