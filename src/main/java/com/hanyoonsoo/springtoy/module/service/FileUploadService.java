package com.hanyoonsoo.springtoy.module.service;

import com.amazonaws.SdkBaseException;
import com.hanyoonsoo.springtoy.module.dto.ImageDto;
import com.hanyoonsoo.springtoy.module.entity.Image;
import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.global.exception.BusinessLogicException;
import com.hanyoonsoo.springtoy.module.global.exception.ErrorCode;
import com.hanyoonsoo.springtoy.module.repository.ImageRepository;
import com.hanyoonsoo.springtoy.module.service.s3.AmazonS3ResourceStorage;
import com.hanyoonsoo.springtoy.module.service.s3.MultipartUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FileUploadService {

    private final AmazonS3ResourceStorage amazonS3ResourceStorage;
    private final ImageRepository imageRepository;

    // File은 업로드 처리를 하고 imageUrl를 반환
    public List<ImageDto> save(User user, List<MultipartFile> multipartFiles){

        List<ImageDto> imageUrls = new ArrayList<>();
        for(MultipartFile multipartFile : multipartFiles){
            verifiedExtension(multipartFile);
            String fullPath = MultipartUtil.createPath(multipartFile);
            String imageUrl = amazonS3ResourceStorage.store(fullPath, multipartFile);
            imageUrls.add(new ImageDto(imageUrl));
            user.addImage(Image.createImage(user, imageUrl));
        }

        return imageUrls;
    }

    public ImageDto save(User user, MultipartFile multipartFile){
        verifiedExtension(multipartFile);
        String fullPath = MultipartUtil.createPath(multipartFile);
        String imageUrl = amazonS3ResourceStorage.store(fullPath, multipartFile);
        user.addImage(Image.createImage(user, imageUrl));
        return new ImageDto(imageUrl);
    }

    private void verifiedExtension(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();

        // 확장자가 jpeg, png인 파일들만 받아서 처리
        if(ObjectUtils.isEmpty(contentType) | (!contentType.contains("image/jpeg") & !contentType.contains("image/png")))
            throw new BusinessLogicException(ErrorCode.EXTENSION_IS_NOT_VALID);
    }

    public String delete(User user, String fireUrl) throws SdkBaseException {
        amazonS3ResourceStorage.delete(fireUrl);

        Image image = imageRepository.findByImageUrl(fireUrl).orElseThrow(() -> new BusinessLogicException(ErrorCode.IMAGE_NOT_FOUND));
        user.removeImage(image);

        imageRepository.delete(image);

        return "이미지 삭제 성공했습니다.";
    }

    public ImageDto update(MultipartFile multipartFile, String fireUrl) throws SdkBaseException{
        amazonS3ResourceStorage.delete(fireUrl);

        Image image = imageRepository.findByImageUrl(fireUrl).orElseThrow(() -> new BusinessLogicException(ErrorCode.IMAGE_NOT_FOUND));

        verifiedExtension(multipartFile);
        String fullPath = MultipartUtil.createPath(multipartFile);
        String imageUrl = amazonS3ResourceStorage.store(fullPath, multipartFile);

        image.setImageUrl(imageUrl);

        return new ImageDto(imageUrl);
    }

}
