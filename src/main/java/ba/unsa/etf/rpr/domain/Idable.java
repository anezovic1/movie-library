package ba.unsa.etf.rpr.domain;

/**
 * Interface that forces all POJO beans to have ID field.
 *
 * @author Anida Nezovic
 */

public interface Idable {

    void setId(int id);

    int getId();
}
