package com.example.Employee_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkingScheduleResponse {
    private MonthInfo monthInfo;
    private List<EmployeeSchedule> schedules;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MonthInfo {
        private String name;
        private int numberOfDays;
        private List<Integer> weekends;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmployeeSchedule {
        private Long employeeId;
        private String employeeName;
        private List<Integer> days; // the days of the month that the employee can go to work for example [1,2,10,12, 30]
        private List<String> statuses; // Morning, Afternoon, Full for each working day in daysOfMonth array   [Morning, Afternoon, Full, Morning, Morning]

    }
}

/*

{
	"month": {
		"name": "March",
		"dayNum": 31,
		"weekends": [4,5,11,12,18,19,26,27]
	},
	"schedules": [
		{
			"name": "Hai",
			"dayOfMonths": [1,2,3,4,5]
			"statuses": ["Morning", "Afternoon", "Full", "Afternoon", "Afternoon"]

		},
		{
			"name": "Vu",
			"dayOfMonths": [1,2,13,14,25]
			"statuses": ["Full", "Afternoon", "Afternoon", "Afternoon", "Afternoon"]
		},
		{
			"name": "Vy",
			"dayOfMonths": [1,2,3,4,5]
			"statuses": ["Morning", "Afternoon", "Full", "Afternoon", "Afternoon"]

		}
	]
}

 */
