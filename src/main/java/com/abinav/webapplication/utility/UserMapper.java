package com.abinav.webapplication.utility;

import com.abinav.webapplication.dto.UserDTO;
import com.abinav.webapplication.model.Follow;
import com.abinav.webapplication.model.Users;
import com.abinav.webapplication.model.Like;
import com.abinav.webapplication.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static UserDTO toDTO(Users user) {
        if (user == null)
            return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setProfilePhotoUrl(user.getProfilePhotoUrl());
        return dto;
    }

    public static List<UserDTO> followersToDTOs(List<Follow> follows) {
        List<UserDTO> list = new ArrayList<>();
        if (follows == null)
            return list;
        for (Follow f : follows) {
            Users follower = f.getFollower();
            list.add(toDTO(follower));
        }
        return list;
    }

    public static List<UserDTO> followingToDTOs(List<Follow> follows) {
        List<UserDTO> list = new ArrayList<>();
        if (follows == null)
            return list;
        for (Follow f : follows) {
            Users following = f.getFollowing();
            list.add(toDTO(following));
        }
        return list;
    }

    public static List<UserDTO> likesToDTOs(List<Like> likes) {
        List<UserDTO> list = new ArrayList<>();
        if (likes == null)
            return list;
        for (Like l : likes) {
            list.add(toDTO(l.getUser()));
        }
        return list;
    }

    public static List<Object> commentsToMaps(List<Comment> comments) {
        List<Object> list = new ArrayList<>();
        if (comments == null)
            return list;
        // DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        for (Comment c : comments) {
            java.util.Map<String, Object> m = new java.util.HashMap<>();
            m.put("id", c.getId());
            m.put("content", c.getContent());
            m.put("createdAt", c.getCreatedAt() != null ? c.getCreatedAt().toString() : null);
            m.put("updatedAt", c.getUpdatedAt() != null ? c.getUpdatedAt().toString() : null);
            m.put("user", toDTO(c.getUser()));
            list.add(m);
        }
        return list;
    }
}
