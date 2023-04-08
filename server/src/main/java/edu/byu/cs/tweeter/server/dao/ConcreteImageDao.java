package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.server.dao.dao_interfaces.ImageDAO;

public class ConcreteImageDao implements ImageDAO {


    @Override
    public String uploadImage(String image,String userAlias) {
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-east-1")
                .build();

        byte[] byteArray = Base64.getDecoder().decode(image);

        ObjectMetadata data = new ObjectMetadata();

        data.setContentLength(byteArray.length);

        data.setContentType("image/jpeg");

        PutObjectRequest request = new PutObjectRequest("my4700craybuck", userAlias,
                new ByteArrayInputStream(byteArray), data).withCannedAcl(CannedAccessControlList.PublicRead);



        s3.putObject(request);



       String link = "https://my4700craybuck.s3.us-east-1.amazonaws.com/" + userAlias;
       return link;
    }
}
