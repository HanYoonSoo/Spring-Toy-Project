package com.hanyoonsoo.springtoy.module.service.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hanyoonsoo.springtoy.module.global.exception.BusinessLogicException;
import com.hanyoonsoo.springtoy.module.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static java.lang.System.exit;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3ResourceStorage {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String store(String fullPath, MultipartFile multipartFile){
        File file = new File(MultipartUtil.getLocalHomeDirectory(), fullPath);

        try{
            multipartFile.transferTo(file);
            amazonS3Client.putObject(new PutObjectRequest(bucket, fullPath, file));
        } catch(Exception e){
            log.error(e.getMessage());
            throw new BusinessLogicException(ErrorCode.FAILED_TO_UPLOAD_FILE);
        } finally {
            if(file.exists())
                removeNewFile(file); // 로컬에 있는 파일 삭제
        }

        return amazonS3Client.getUrl(bucket, fullPath).toString(); // S3에 업로드된 이미지 URL 반환
    }


    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) log.info("The file has been deleted.");
        else {
            log.info("Failed to delete file.");
        }
    }

    public void delete(String fileUrl) {
        try {
            String key = fileUrl.substring(62);
            try {
                amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
            } catch (AmazonServiceException e) {
                log.error(e.getErrorMessage());
                exit(1);
            }
            log.info(String.format("[%s] deletion complete", key));
        } catch (Exception e) {
            throw new BusinessLogicException(ErrorCode.FAILED_TO_DELETE_FILE);
        }
    }

}
