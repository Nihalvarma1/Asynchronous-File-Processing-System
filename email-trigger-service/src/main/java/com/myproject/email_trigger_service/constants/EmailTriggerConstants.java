package com.myproject.email_trigger_service.constants;

public class EmailTriggerConstants {
    public static String MAIL_SUBJECT="CLAIMS_PROCESSING_REPORT- PA";
    public static String MAIL_BODY="Hi Team,\nPlease find the attached balance control report for CLAIMS_PROCESSING_REPORT.\nReport Location: \\ghp.com\\hub\\GHP\\HHOCIL\\PROD\\PA\\Outbound\\BOTH\\MONTHLY\\CLAIMS_PROCESSING_REPORT\\FILE\n\nEnvironment: PROD\n\nThanks,\nIPLUS Team\n\n****Please do not reply to this email****";
    public static String MAIL_FROM="integrationuser@highmarkwholecare.com";
    public static String CAID_REGEX="^CAID_CLAIMS_PAID_PROCESSING_SUMMARY_REPORT_\\d{14}\\.csv$";
}
