package com.hanyoonsoo.springtoy.module.controller;

import com.hanyoonsoo.springtoy.module.dto.*;
import com.hanyoonsoo.springtoy.module.global.security.CustomUserDetails;
import com.hanyoonsoo.springtoy.module.service.FileUploadService;
import com.hanyoonsoo.springtoy.module.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FileUploadService fileUploadService;

    @PostMapping("/signup")
    public ResponseEntity signUp(@Valid @RequestBody UserDto.SignUp signUpDto){
        UserDto.Response reponse = userService.signUp(signUpDto);

        return new ResponseEntity<>(new SingleResponseDto<>(reponse), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getUser(@AuthenticationPrincipal CustomUserDetails user) {
        String email = user.getEmail();
        UserDto.Response response = userService.userDtoResponse(email);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity patchUser(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody UserPatchDto patchDto) {
        String email = user.getEmail();

        UserDto.Response response = userService.updateUser(email, patchDto);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @PostMapping("/emails/verification-requests")
    public ResponseEntity sendMessage(@RequestParam("email") @Valid String email){
        userService.sendCodeToEmail(email);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/emails/verifications")
    public ResponseEntity verificationEmail(@RequestParam("email") @Valid String email,
                                            @RequestParam("code") String authCode){
        EmailVerificationResult response = userService.verifiedCode(email, authCode);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }


    @PostMapping("/images")
    public ResponseEntity setProfileImages(@AuthenticationPrincipal CustomUserDetails user,
                                        @RequestPart("file") List<MultipartFile> multipartFiles){

        List<ImageDto> response = userService.profileImagesSave(user.getEmail(), multipartFiles);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @PostMapping("/image")
    public ResponseEntity setProfileImage(@AuthenticationPrincipal CustomUserDetails user,
                                           @RequestPart("file") MultipartFile multipartFile){

        ImageDto response = userService.profileImageSave(user.getEmail(), multipartFile);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @DeleteMapping("/image")
    public ResponseEntity removeImage(@AuthenticationPrincipal CustomUserDetails user,
                                      @RequestParam("imageUrl") String imageUrl){
        String response = userService.delete(user.getEmail(), imageUrl);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping("/images")
    public ResponseEntity findUserWithImages(@AuthenticationPrincipal CustomUserDetails user){
        List<ImageDto> response = userService.findUserWithImages(user.getEmail());

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @PatchMapping("/image")
    public ResponseEntity updateProfileImage(@RequestPart("file") MultipartFile multipartFile,
                                             @RequestParam("imageUrl") String imageUrl){
        ImageDto response = userService.update(multipartFile, imageUrl);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }
}
