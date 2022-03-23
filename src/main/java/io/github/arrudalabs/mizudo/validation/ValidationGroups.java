package io.github.arrudalabs.mizudo.validation;

import javax.validation.groups.Default;

public interface ValidationGroups {
    public static interface OnPost extends Default{}
    public static interface OnPut extends Default{}
}
