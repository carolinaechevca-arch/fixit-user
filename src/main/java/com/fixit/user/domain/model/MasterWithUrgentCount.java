package com.fixit.user.domain.model;

public record MasterWithUrgentCount(Technician master, long urgentCount) {
}