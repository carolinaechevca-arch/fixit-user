package com.fixit.user.domain.model;


import com.fixit.user.domain.enums.TechnicianCategory;
import com.fixit.user.domain.enums.TechnicianStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Technician {
    User user;
    TechnicianCategory category;
    TechnicianStatus status;
    Integer taskCount;
    Integer currentPoints;

    public static Technician createNew(User user, TechnicianCategory category) {
        return Technician.builder()
                .user(user)
                .category(category)
                .status(TechnicianStatus.AVAILABLE)
                .taskCount(0)
                .currentPoints(0)
                .build();
    }

    public Integer getAvailablePoints() {
        int maxPoints = category.getMaxPoints();
        return (maxPoints == 0) ? 0 : maxPoints - this.currentPoints;
    }
}