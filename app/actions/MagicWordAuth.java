package actions;

import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author Michael Vaughan
 */
public class MagicWordAuth extends Action.Simple {

    @Override
    public CompletionStage<Result> call(Context context) {
        if (!"please".equalsIgnoreCase(context.request().getHeader("Authorization"))) {
            return CompletableFuture.completedFuture(unauthorized("You didn't say the magic word."));
        }
        return delegate.call(context);
    }
}
