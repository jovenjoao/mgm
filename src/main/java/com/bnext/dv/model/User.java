package com.bnext.dv.model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

    @NonNull
    private String user;

    private List<String> contacts;

    public String getId (){
        return user;
    }

    public void setId (String user){
        this.user = user;
    }

}
