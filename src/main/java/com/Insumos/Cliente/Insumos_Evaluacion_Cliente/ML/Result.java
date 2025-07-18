package com.Insumos.Cliente.Insumos_Evaluacion_Cliente.ML;

import java.util.List;

public class Result<T> {

    public boolean correct;
    public String errorMessage;
    public Exception ex;
    public T object;

    public Result() {
    }

    public Result(boolean correct, String errorMessage, Exception ex, T object) {
        this.correct = correct;
        this.errorMessage = errorMessage;
        this.ex = ex;
        this.object = object;
    }
}
