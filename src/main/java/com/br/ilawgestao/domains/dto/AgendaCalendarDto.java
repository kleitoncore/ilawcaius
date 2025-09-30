package com.br.ilawgestao.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaCalendarDto {
	private long id;
    private String title;
    private String start;
    private String status;
    private String color;
    private String textColor;
    private String img;
    private String image_url;
}
