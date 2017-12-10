
package wad.service.verifiers;

import java.util.List;

public interface Verifier <T>{
    public List<String> verifyNew(T t);
    public List<String> verifyDelete(T t);
    public List<String> warn(T t);
}
