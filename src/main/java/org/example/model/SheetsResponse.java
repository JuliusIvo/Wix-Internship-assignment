package org.example.model;

import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SheetsResponse {
    private String submissionUrl;
    private List<Sheet> sheets;
}
