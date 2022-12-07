package com.gudratli.kalemyazilimtask.exception;

public class DuplicateException extends Exception
{
    private static final String defaultMessage = "There is duplicate property.";

    public DuplicateException ()
    {
        super(defaultMessage);
    }

    public DuplicateException (String message)
    {
        super(message);
    }
}
