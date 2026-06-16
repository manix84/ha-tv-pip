import { renderToStaticMarkup } from "react-dom/server";
import { describe, expect, it } from "vitest";

import { StatusBadge } from "./StatusBadge";

describe("StatusBadge", () => {
  it("renders the complete label", () => {
    const markup = renderToStaticMarkup(<StatusBadge status="complete" />);

    expect(markup).toContain("Complete");
  });

  it("renders future status copy", () => {
    const markup = renderToStaticMarkup(<StatusBadge status="future" />);

    expect(markup).toContain("Future");
  });
});
