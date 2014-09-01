package contentproviders;

/**
 * 1. Sync/Async provide methods
 */
public abstract class ContentProvider<Req, Content> {

    abstract public Content get(Req req);

}
