package com.aosp_repo.update;

import java.util.List;

public record UpdateData(String tag, boolean preRelease, List<UpdateFile> updateFiles, String body) {
}
