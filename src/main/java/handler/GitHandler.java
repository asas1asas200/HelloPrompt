package handler;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;

import snippets.ANSIColor;
import snippets.GitImage;

/**
 * This handler can read current directory git repository info.<br>
 *
 * @see Handler
 *
 * @author <a href="mailto:rexwu.1123@gmail.com">Zeng</a>
 */
public class GitHandler extends Handler {
    private class GitInfo {
        public final String branch;
        public final int added;
        public final int uncommitedChanges;
        public final String pwd;
        public final Boolean hasConflict;

        public GitInfo(String branch, int added, int uncommitedChanges, String pwd, Boolean hasConflict) {
            this.branch = branch;
            this.added = added;
            this.uncommitedChanges = uncommitedChanges;
            this.pwd = pwd;
            this.hasConflict = hasConflict;
        }
    }

    private GitInfo getGitInfo(File dir) {
        StringBuilder gitInfo = new StringBuilder();
        try {
            Git git = Git.open(dir);
            Repository repository = git.getRepository();
            Status status = new Git(repository).status().call();
            GitInfo info = new GitInfo(repository.getBranch(), status.getAdded().size(),
                    status.getUncommittedChanges().size(), dir.getName(), status.getConflicting().size() > 0);
            return info;
        } catch (Exception e) {
            return null;
        }
    }

    private String render() {
        StringBuilder output = new StringBuilder();

        Path currentRelativePath = Paths.get("");
        File dir = new File(currentRelativePath.toAbsolutePath().toString());
        GitInfo info = null;
        List<String> logo = GitImage.getLogo();
        while (dir.getPath() != "/") {
            info = getGitInfo(dir);
            if (info != null)
                break;
            dir = new File(dir.getParent());
        }
        if (info != null) {

            output.append(logo.get(0));
            output.append(" | Repo: " + (info.uncommitedChanges > 0 ? ANSIColor.RED : ANSIColor.GREEN) + info.pwd
                    + ANSIColor.RESET + "\n");

            output.append(logo.get(1) + " |_____________\n");

            output.append(logo.get(2));
            output.append(" | Branch Name: " + ANSIColor.BLUE + info.branch + ANSIColor.RESET + "\n");

            output.append(logo.get(3));
            output.append(" | Added: " + info.added + "\n");

            output.append(logo.get(4));
            output.append(" | Uncommited Changes: " + ANSIColor.RED + info.uncommitedChanges + ANSIColor.RESET + "\n");

            output.append(logo.get(5));
            output.append(" | Has Conflict: " + (info.hasConflict ? ANSIColor.RED + "YES" : ANSIColor.GREEN + "NO")
                    + ANSIColor.GREEN + "\n");

            output.append(logo.get(6) + " |\n");

        } else {
            List<String> noRepoImg = GitImage.getNOREPO();
            for (int i = 0; i < 7; i++) {
                output.append(logo.get(i) + " |" + noRepoImg.get(i) + "\n");
            }
        }
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
