package com.example.mzting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCustomImage {

    @Id
    private Long id;

    @Column(nullable = false)
    private String profileImage1 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile1.jpg?alt=media&token=d9a25fd3-78d0-480b-8f94-249287cd80ad";

    @Column(nullable = false)
    private String profileImage2 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile2.jpg?alt=media&token=8d49e074-32c2-4f10-b160-42ad1dc40dc2";

    @Column(nullable = false)
    private String profileImage3 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile3.jpg?alt=media&token=4a33319e-4640-48cb-a60f-a08cffed8b5e";

    @Column(nullable = false)
    private String profileImage4 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile4.jpg?alt=media&token=1a3bb87c-41fb-4da7-9a68-fe84e21f9c01";

    @Column(nullable = false)
    private String profileImage5 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile5.jpg?alt=media&token=f13f685e-8b49-4895-b994-6960c52f07c0";

    @Column(nullable = false)
    private String profileImage6 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile6.jpg?alt=media&token=afb51646-ef08-45f8-a18c-a0d87ec9e063";

    @Column(nullable = false)
    private String profileImage7 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile7.jpg?alt=media&token=0fe9496b-2c84-477e-aea3-a849094cd1b1";

    @Column(nullable = false)
    private String profileImage8 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile8.jpg?alt=media&token=c9332766-e707-473d-b5ad-76cf2e962034";

    @Column(nullable = false)
    private String profileImage9 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile9.jpg?alt=media&token=3a46c193-47d1-4403-a160-9484cf7cce2f";

    @Column(nullable = false)
    private String profileImage10 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile10.jpg?alt=media&token=caa1510e-b8d1-478b-8658-1332f75dbb72";

    @Column(nullable = false)
    private String profileImage11 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile11.jpg?alt=media&token=13c8bf70-5dd2-494c-80cf-ea9705f38f54";

    @Column(nullable = false)
    private String profileImage12 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile12.jpg?alt=media&token=87852df7-e4ae-4d73-9c9e-ea07064fe3b5";

    @Column(nullable = false)
    private String profileImage13 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile13.jpg?alt=media&token=27c31f66-2bd9-4576-b7e2-b03903829144";

    @Column(nullable = false)
    private String profileImage14 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile14.jpg?alt=media&token=84805377-fbcd-411d-9ed4-dc62235a3d91";

    @Column(nullable = false)
    private String profileImage15 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile15.jpg?alt=media&token=67c1004e-034c-4f1b-b2ab-e66af484c99d";

    @Column(nullable = false)
    private String profileImage16 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile16.jpg?alt=media&token=31c2bc08-91fa-45fe-8f43-da6cfd9b2067";

    public void setProfileImage(int index, String value) {
        switch (index) {
            case 1: profileImage1 = value; break;
            case 2: profileImage2 = value; break;
            case 3: profileImage3 = value; break;
            case 4: profileImage4 = value; break;
            case 5: profileImage5 = value; break;
            case 6: profileImage6 = value; break;
            case 7: profileImage7 = value; break;
            case 8: profileImage8 = value; break;
            case 9: profileImage9 = value; break;
            case 10: profileImage10 = value; break;
            case 11: profileImage11 = value; break;
            case 12: profileImage12 = value; break;
            case 13: profileImage13 = value; break;
            case 14: profileImage14 = value; break;
            case 15: profileImage15 = value; break;
            case 16: profileImage16 = value; break;
            default: throw new IllegalArgumentException("Invalid profile image index: " + index);
        }
    }
}