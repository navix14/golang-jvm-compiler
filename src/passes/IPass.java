package passes;

import java.util.List;

import common.Error;

public interface IPass {
    public List<Error> getErrors();
    public void execute();
}