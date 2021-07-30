package se24.borrowservice.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UpdateRulesRequest {
    private int maxAmountTeacher;
    private int maxAmountUndergra;
    private int maxAmountPostgra;

    private int borrowTimeTeacher;
    private int borrowTimeUndergra;
    private int borrowTimePostgra;

    private int subscribeTimeTeacher;
    private int subscribeTimeUndergra;
    private int subscribeTimePostgra;
}
