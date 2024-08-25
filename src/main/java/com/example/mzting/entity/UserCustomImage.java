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
    private String profileImage1 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile1.png?alt=media&token=6e14d9d5-8977-408a-a4b4-f9bbd38dafcd";

    @Column(nullable = false)
    private String profileImage2 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile2.png?alt=media&token=9b355fa8-1153-4e2f-8917-b54b1aa315f3";

    @Column(nullable = false)
    private String profileImage3 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile3.png?alt=media&token=70c153f2-6102-4430-81e5-d67653fab994";

    @Column(nullable = false)
    private String profileImage4 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile4.png?alt=media&token=c4cd72af-3d63-429a-b820-5ec1811f2f28";

    @Column(nullable = false)
    private String profileImage5 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile5.png?alt=media&token=77ddc879-5e2f-4035-863b-060991a418e8";

    @Column(nullable = false)
    private String profileImage6 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile6.png?alt=media&token=380ea951-4cf2-4f32-808c-b8b445656ceb";

    @Column(nullable = false)
    private String profileImage7 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile7.png?alt=media&token=7493ca40-88bc-44b3-8a1f-8daac6118bc9";

    @Column(nullable = false)
    private String profileImage8 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile8.png?alt=media&token=8ae609e6-7e23-49e6-8ae0-afa4471c337e";

    @Column(nullable = false)
    private String profileImage9 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile9.png?alt=media&token=3c7ffc6c-80ae-473c-80ae-e8c6352f3851";

    @Column(nullable = false)
    private String profileImage10 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile10.png?alt=media&token=d11e2aa8-69d3-4bbc-9f59-14e1eb111e4e";

    @Column(nullable = false)
    private String profileImage11 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile11.png?alt=media&token=6c45f4f4-d3d0-46cc-8521-9e12fc903f63";

    @Column(nullable = false)
    private String profileImage12 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile12.png?alt=media&token=5e584807-4c4f-4f58-a8da-7a8e5d4a33b2";

    @Column(nullable = false)
    private String profileImage13 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile13.png?alt=media&token=ea194f33-f4ea-41aa-b4bd-319092bb5ffd";

    @Column(nullable = false)
    private String profileImage14 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile14.png?alt=media&token=b9eb678c-119a-497f-9288-50f7284a705c";

    @Column(nullable = false)
    private String profileImage15 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile15.png?alt=media&token=383cf414-f87b-46de-bbc4-eca6ec8b5bac";

    @Column(nullable = false)
    private String profileImage16 = "https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2Fprofile16.png?alt=media&token=91a4388f-7b6b-47f6-85a3-4d148cc225f1";

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

    public String getProfileImage(int index) {
        switch (index) {
            case 1: return profileImage1;
            case 2: return profileImage2;
            case 3: return profileImage3;
            case 4: return profileImage4;
            case 5: return profileImage5;
            case 6: return profileImage6;
            case 7: return profileImage7;
            case 8: return profileImage8;
            case 9: return profileImage9;
            case 10: return profileImage10;
            case 11: return profileImage11;
            case 12: return profileImage12;
            case 13: return profileImage13;
            case 14: return profileImage14;
            case 15: return profileImage15;
            case 16: return profileImage16;
            default: throw new IllegalArgumentException("Invalid profile image index: " + index);
        }
    }
}