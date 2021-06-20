package handler;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;

/**
 * This handler can read current directory git repository info.<br>
 *
 * @see Handler
 *
 * @author <a href="mailto:rexwu.1123@gmail.com">Zeng</a>
 */
public class GitHandler extends Handler {

    private String getGitInfo(File dir) {
        StringBuilder gitInfo = new StringBuilder();
        try {
            Git git = Git.open(dir);
            Repository repository = git.getRepository();
            Status status = new Git(repository).status().call();
            gitInfo.append("\tBranch: " + repository.getBranch() + "\n");
            gitInfo.append("\tAdded: " + status.getAdded() + "\n");
            gitInfo.append("\tChanged: " + status.getChanged() + "\n");
            gitInfo.append("\tConflicting: " + status.getConflicting() + "\n");
            gitInfo.append("\tConflictingStageState: " + status.getConflictingStageState() + "\n");
            gitInfo.append("\tIgnoredNotInIndex: " + status.getIgnoredNotInIndex() + "\n");
            gitInfo.append("\tMissing: " + status.getMissing() + "\n");
            gitInfo.append("\tModified: " + status.getModified() + "\n");
            gitInfo.append("\tRemoved: " + status.getRemoved() + "\n");
            gitInfo.append("\tUntracked: " + status.getUntracked() + "\n");
            gitInfo.append("\tUntrackedFolders: " + status.getUntrackedFolders() + "\n");
            gitInfo.append("\tUncommittedChanges: " + status.getUncommittedChanges() + "\n");
            gitInfo.append("\tHasUncommittedChanges: " + status.hasUncommittedChanges() + "\n");
            return gitInfo.toString();
        } catch (Exception e) {
            return "No any repository in current directory!";
        }
    }

    private String render() {
        StringBuilder output = new StringBuilder();

        Path currentRelativePath = Paths.get("");
        File dir = new File(currentRelativePath.toAbsolutePath().toString());

        output.append("Git Info in current directory: \n");
        output.append(getGitInfo(dir));

        return output.toString();
    }

    /**
     * Initialize GitHandler.
     */
    public GitHandler() {

    }

    @Override
    public void run() {
        ifOutput = true;
        result = render();
    }
}
