package com.school.user.controller;

import com.school.user.dto.request.CreateParentProfileRequest;
import com.school.user.dto.request.CreateStaffProfileRequest;
import com.school.user.dto.request.CreateStudentProfileRequest;
import com.school.user.dto.request.CreateTeacherProfileRequest;
import com.school.user.dto.request.LinkParentStudentRequest;
import com.school.user.dto.request.UpdateParentProfileRequest;
import com.school.user.dto.request.UpdateStaffProfileRequest;
import com.school.user.dto.request.UpdateStudentProfileRequest;
import com.school.user.dto.request.UpdateTeacherProfileRequest;
import com.school.user.dto.response.ParentStudentLinkResponse;
import com.school.user.dto.response.UserProfileResponse;
import com.school.user.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping("/students")
    public ResponseEntity<UserProfileResponse> createStudentProfile(
            @Valid @RequestBody CreateStudentProfileRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userProfileService.createStudentProfile(request));
    }

    @PostMapping("/teachers")
    public ResponseEntity<UserProfileResponse> createTeacherProfile(
            @Valid @RequestBody CreateTeacherProfileRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileService.createTeacherProfile(request));
    }

    @PostMapping("/parents")
    public ResponseEntity<UserProfileResponse> createParentProfile(
            @Valid @RequestBody CreateParentProfileRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileService.createParentProfile(request));
    }

    @PostMapping("/staff")
    public ResponseEntity<UserProfileResponse> createStaffProfile(
            @Valid @RequestBody CreateStaffProfileRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileService.createStaffProfile(request));
    }

    @PutMapping("/students/{authUserId}")
    public ResponseEntity<UserProfileResponse> updateStudentProfile(
            @PathVariable UUID authUserId,
            @Valid @RequestBody UpdateStudentProfileRequest request
    ) {
        return ResponseEntity.ok(userProfileService.updateStudentProfile(authUserId, request));
    }

    @PutMapping("/teachers/{authUserId}")
    public ResponseEntity<UserProfileResponse> updateTeacherProfile(
            @PathVariable UUID authUserId,
            @Valid @RequestBody UpdateTeacherProfileRequest request
    ) {
        return ResponseEntity.ok(userProfileService.updateTeacherProfile(authUserId, request));
    }

    @PutMapping("/parents/{authUserId}")
    public ResponseEntity<UserProfileResponse> updateParentProfile(
            @PathVariable UUID authUserId,
            @Valid @RequestBody UpdateParentProfileRequest request
    ) {
        return ResponseEntity.ok(userProfileService.updateParentProfile(authUserId, request));
    }

    @PutMapping("/staff/{authUserId}")
    public ResponseEntity<UserProfileResponse> updateStaffProfile(
            @PathVariable UUID authUserId,
            @Valid @RequestBody UpdateStaffProfileRequest request
    ) {
        return ResponseEntity.ok(userProfileService.updateStaffProfile(authUserId, request));
    }

    @GetMapping("/by-auth-user/{authUserId}")
    public ResponseEntity<UserProfileResponse> getProfileByAuthUserId(@PathVariable UUID authUserId) {
        return ResponseEntity.ok(userProfileService.getProfileByAuthUserId(authUserId));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserProfileResponse> getProfileByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userProfileService.getProfileByUsername(username));
    }

    @PostMapping("/parents/link-student")
    public ResponseEntity<ParentStudentLinkResponse> linkParentAndStudent(
            @Valid @RequestBody LinkParentStudentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileService.linkParentAndStudent(request));
    }

    @GetMapping("/parents/{parentProfileId}/students")
    public ResponseEntity<List<UserProfileResponse>> getLinkedStudentsForParent(
            @PathVariable UUID parentProfileId
    ) {
        return ResponseEntity.ok(userProfileService.getLinkedStudentsForParent(parentProfileId));
    }

    @GetMapping("/students/{studentProfileId}/parents")
    public ResponseEntity<List<UserProfileResponse>> getLinkedParentsForStudent(
            @PathVariable UUID studentProfileId
    ) {
        return ResponseEntity.ok(userProfileService.getLinkedParentsForStudent(studentProfileId));
    }
}
