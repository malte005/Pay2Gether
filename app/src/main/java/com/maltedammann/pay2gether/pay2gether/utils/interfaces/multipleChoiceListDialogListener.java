package com.maltedammann.pay2gether.pay2gether.utils.interfaces;

import java.util.List;

/**
 * Created by damma on 10.01.2017.
 */

public interface multipleChoiceListDialogListener {
    void onOkay(List<String> userIds);

    void onCencel();
}
