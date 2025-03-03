package com.example.scheduling.dto;

import java.util.UUID;

public class AvailableTimesRequestDTO {

        private UUID serviceId;
        private UUID businessId;
        private String date;

        public UUID getServiceId() {
            return serviceId;
        }


        public UUID getBusinessId() {
            return businessId;
        }

        public String getDate() {
            return date;
        }


}
