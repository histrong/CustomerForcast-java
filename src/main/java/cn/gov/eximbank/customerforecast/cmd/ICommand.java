package cn.gov.eximbank.customerforecast.cmd;

public interface ICommand {

    String getCommandName();

    void execute();
}
